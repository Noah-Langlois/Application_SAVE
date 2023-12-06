package main

import (
	"encoding/json"
	"fmt"
	"strconv"

	abcitypes "github.com/tendermint/tendermint/abci/types"
)

type s_tx struct {
	TypeTx string
	Date   int

	IdClient string
	IdConv   string
	IdMsg    string

	Cont string
}

func empty_tx() s_tx {
	var ans s_tx

	ans.TypeTx = ""
	ans.Date = 0
	ans.IdClient = ""
	ans.IdConv = ""
	ans.IdMsg = ""
	ans.Cont = ""

	return ans

}

func toEventABCI(tx s_tx) []abcitypes.Event {
	fmt.Println("toEventABCI -------------- ")

	events := []abcitypes.Event{
		{
			Type: "transfer",
			Attributes: []abcitypes.EventAttribute{
				{Key: []byte("type_tx"), Value: []byte(tx.TypeTx), Index: true},
				{Key: []byte("date"), Value: []byte(strconv.Itoa(tx.Date)), Index: true},
				{Key: []byte("id_client"), Value: []byte(tx.IdClient), Index: true},
				{Key: []byte("id_conv"), Value: []byte(tx.IdConv), Index: true},
				{Key: []byte("id_msg"), Value: []byte(tx.IdMsg), Index: true},
				{Key: []byte("cont"), Value: []byte(tx.Cont), Index: true},
			},
		},
	}

	return events
}

func create_tx(jsoninput []byte) s_tx {
	fmt.Println("create_tx -------------- ")
	var ans s_tx
	err := json.Unmarshal(jsoninput, &ans)
	if err != nil {
		fmt.Println(err)
		return empty_tx()
	}

	fmt.Println(ans)
	return ans
}

func check_tx(ans s_tx) bool {
	fmt.Println("check_tx -------------- ")
	if ans.IdClient == "" {
		fmt.Println("my_tx n'est pas bon, IdClient invalid")
		return false
	}

	if ans.TypeTx == "" {
		fmt.Println("my_tx il est pas bon, TypeTx invalid")
		return false
	}

	if ans.Date == 0 {
		fmt.Println("my_tx il est pas bon, Date invalid")
		return false
	}

	//TODO: check

	

	return true
}
