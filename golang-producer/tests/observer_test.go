package tests

import (
	"testing"
)

func testMockObserverFiles(t *testing.T) {
	res := 2
	if 1+1 != res {
		t.Error("not equal")
	}
}
