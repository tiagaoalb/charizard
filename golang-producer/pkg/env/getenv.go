package env

import (
	"fmt"

	"github.com/spf13/viper"
)

func Getenv(env string) string {
	loadDefaults()
	viper.AutomaticEnv()
	return viper.GetString(env)
}

func loadDefaults() {
	viper.SetDefault("RABBITMQ_HOST", "localhost")
	viper.SetDefault("RABBITMQ_PORT", "5672")
	viper.SetDefault("RABBITMQ_USER", "guest")
	viper.SetDefault("RABBITMQ_PASS", "guest")
	viper.SetDefault("RABBITMQ_VHOST", "/")
	viper.SetDefault("RABBITMQ_CONCILIATION_QUEUE", "conciliation")
	viper.SetDefault("RABBITMQ_CONCILIATION_DLX", "conciliation.dlx")
	viper.SetDefault("RABBITMQ_TRANSACTION_QUEUE", "transaction")
	viper.SetDefault("RABBITMQ_TRANSACTION_DLX", "transaction.dlx")

}
func logLoadedEnv() {
	fmt.Println("Loaded Environment Variables:")
	for _, key := range viper.AllKeys() {
		fmt.Println(viper.AllKeys())
		value := viper.Get(key)
		fmt.Printf("%s: %v\n", key, value)
	}

}
