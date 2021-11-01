(ns bank-kata-with-clojure.acceptance-test
  (:require [clojure.test :refer :all]
            [bank-kata-with-clojure.account :refer :all])
  (:require [bank-kata-with-clojure.fixtures :refer [return-println]]))

(defn print-statements [account]
  (print-bank-statements return-println account))

(deftest acceptance-test
  (testing "should print the bank statement for an account after some deposits and withdraws"
    (let [account (map->Account {:transactions '()})]
      (is
        (=
          (->> account
               (deposit 1000 (on "10-01-2012"))
               (deposit 2000 (on "13-01-2012"))
               (withdraw 500 (on "14-01-2012"))
               (print-statements))
          "date || credit || debit || balance\n14/01/2012 || || 500.00 || 2500.00\n13/01/2012 || 2000.00 || || 3000.00\n10/01/2012 || 1000.00 || || 1000.00\n")))))

