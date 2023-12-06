package main

import (
	"bytes"
	"fmt"

	"github.com/dgraph-io/badger"
	abcitypes "github.com/tendermint/tendermint/abci/types"
)

type KVStoreApplication struct {
	db           *badger.DB
	currentBatch *badger.Txn
}

var _ abcitypes.Application = (*KVStoreApplication)(nil)

func NewKVStoreApplication(db *badger.DB) *KVStoreApplication {
	return &KVStoreApplication{
		db: db,
	}
}

func (KVStoreApplication) Info(req abcitypes.RequestInfo) abcitypes.ResponseInfo {
	return abcitypes.ResponseInfo{}
}

func (KVStoreApplication) SetOption(req abcitypes.RequestSetOption) abcitypes.ResponseSetOption {
	return abcitypes.ResponseSetOption{}
}

func (app *KVStoreApplication) DeliverTx(req abcitypes.RequestDeliverTx) abcitypes.ResponseDeliverTx {
	fmt.Println("DeliverTx -------------- ")

	code := app.isValid(req.Tx)
	if code != 0 {
		return abcitypes.ResponseDeliverTx{Code: code}
	}

	//parts := bytes.Split(req.Tx, []byte("="))
	//key, value := parts[0], parts[1]

	key, value := []byte("0"), req.Tx

	var my_tx s_tx
	my_tx = create_tx(req.Tx)
	if !check_tx(my_tx) {
		return abcitypes.ResponseDeliverTx{Code: 12}
	}

	err := app.currentBatch.Set(key, value)
	if err != nil {
		panic(err)
	}

	events := toEventABCI(my_tx)

	// events := []abcitypes.Event{
	// 	{
	// 		Type: "transfer",
	// 		Attributes: []abcitypes.EventAttribute{
	// 			{Key: []byte("idclient"), Value: []byte("Bob"), Index: true},
	// 			{Key: []byte("idconv"), Value: []byte("Alice"), Index: true},
	// 			{Key: []byte("idmsg"), Value: []byte("Alice"), Index: true},
	// 			{Key: []byte("typemsg"), Value: []byte("100"), Index: true},
	// 		},
	// 	},
	// }

	return abcitypes.ResponseDeliverTx{Code: 0, Events: events}
}

func (app *KVStoreApplication) isValid(tx []byte) (code uint32) {
	// check format
	// parts := bytes.Split(tx, []byte("="))
	// if len(parts) != 2 {
	// 	return 1
	// }

	// key, value := parts[0], parts[1]

	key, value := []byte("0"), tx

	// check format
	var my_tx s_tx
	my_tx = create_tx(tx)
	if !check_tx(my_tx) {
		return 12
	}

	// check if the same key=value already exists
	err := app.db.View(func(txn *badger.Txn) error {
		item, err := txn.Get(key)
		if err != nil && err != badger.ErrKeyNotFound {
			return err
		}
		if err == nil {
			return item.Value(func(val []byte) error {
				if bytes.Equal(val, value) {
					code = 2
				}
				return nil
			})
		}
		return nil
	})
	if err != nil {
		panic(err)
	}

	return code
}

func (app *KVStoreApplication) CheckTx(req abcitypes.RequestCheckTx) abcitypes.ResponseCheckTx {
	fmt.Println("CheckTx -------------- ")
	fmt.Println("req :" + string(req.Tx))
	code := app.isValid(req.Tx)
	fmt.Printf("code : %d\n", code)
	return abcitypes.ResponseCheckTx{Code: code, GasWanted: 1}
}

func (app *KVStoreApplication) Commit() abcitypes.ResponseCommit {
	app.currentBatch.Commit()
	return abcitypes.ResponseCommit{Data: []byte{}}
}

func (app *KVStoreApplication) Query(reqQuery abcitypes.RequestQuery) (resQuery abcitypes.ResponseQuery) {
	resQuery.Key = reqQuery.Data
	fmt.Println("Query -------------- ")
	err := app.db.View(func(txn *badger.Txn) error {
		item, err := txn.Get(reqQuery.Data)
		if err != nil && err != badger.ErrKeyNotFound {
			return err
		}
		if err == badger.ErrKeyNotFound {
			resQuery.Log = "does not exist"
		} else {
			return item.Value(func(val []byte) error {
				resQuery.Log = "exists"
				resQuery.Value = val
				return nil
			})
		}
		return nil
	})
	if err != nil {
		panic(err)
	}
	return
}

func (KVStoreApplication) InitChain(req abcitypes.RequestInitChain) abcitypes.ResponseInitChain {
	return abcitypes.ResponseInitChain{}
}

func (app *KVStoreApplication) BeginBlock(req abcitypes.RequestBeginBlock) abcitypes.ResponseBeginBlock {
	app.currentBatch = app.db.NewTransaction(true)
	return abcitypes.ResponseBeginBlock{}
}

func (KVStoreApplication) EndBlock(req abcitypes.RequestEndBlock) abcitypes.ResponseEndBlock {
	return abcitypes.ResponseEndBlock{}
}

func (KVStoreApplication) ListSnapshots(abcitypes.RequestListSnapshots) abcitypes.ResponseListSnapshots {
	return abcitypes.ResponseListSnapshots{}
}

func (KVStoreApplication) OfferSnapshot(abcitypes.RequestOfferSnapshot) abcitypes.ResponseOfferSnapshot {
	return abcitypes.ResponseOfferSnapshot{}
}

func (KVStoreApplication) LoadSnapshotChunk(abcitypes.RequestLoadSnapshotChunk) abcitypes.ResponseLoadSnapshotChunk {
	return abcitypes.ResponseLoadSnapshotChunk{}
}

func (KVStoreApplication) ApplySnapshotChunk(abcitypes.RequestApplySnapshotChunk) abcitypes.ResponseApplySnapshotChunk {
	return abcitypes.ResponseApplySnapshotChunk{}
}
