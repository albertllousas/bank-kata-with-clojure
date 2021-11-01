(ns bank-kata-with-clojure.account-test
  (:require [clojure.test :refer :all])
  (:require [bank-kata-with-clojure.account :refer [on deposit ->Account withdraw calculate-balances print-bank-statements]])
  (:import [java.time LocalDate DateTimeException])
  (:require [bank-kata-with-clojure.fixtures :refer [catch-ex-info return-println]]))

(deftest on-test

  (testing "should create a local-date from an string"
    (is (= (on "01-10-2020") (LocalDate/of 2020 10 1))))

  (testing "should fail creating a local-date from an invalid string"
    (is (thrown? DateTimeException (on "invalid local date")))))

(deftest deposit-test

  (testing "should fail depositing negative amount"
    (is
      (=
        (catch-ex-info (deposit -1000 (on "01-10-2020") (->Account '())))
        {:msg "Invalid amount", :data {:type :invalid-amount}})))

  (testing "should deposit money in an empty account"
    (is
      (=
        (deposit 1000 (on "01-10-2020") (->Account '()))
        (->Account (list {:amount 1000 :local-date (LocalDate/of 2020 10 1) :type :deposit})))))

  (testing "should deposit money in an account with some money already"
    (is
      (=
        (deposit 400 (on "02-10-2020") (->Account (list {:amount 100 :local-date (LocalDate/of 2020 10 1) :type :deposit})))
        (->Account (list
                     {:amount 100 :local-date (LocalDate/of 2020 10 1) :type :deposit}
                     {:amount 400 :local-date (LocalDate/of 2020 10 2) :type :deposit})))))

  (testing "should fail depositing money in an account with previous transactions when date is in the past"
    (is
      (=
        (catch-ex-info (deposit 1 (on "01-09-2020") (->Account (list {:amount 1 :local-date (LocalDate/of 2020 10 1) :type :deposit}))))
        {:msg "Non valid transaction date", :data {:type :non-valid-transaction-date}}))))

(deftest withdraw-test

  (testing "should fail withdrawing negative amount"
    (is
      (=
        (catch-ex-info (withdraw -1000 (on "01-10-2020") (->Account '())))
        {:msg "Invalid amount", :data {:type :invalid-amount}})))

  (testing "should withdraw money in an empty account"
    (is
      (=
        (withdraw 1000 (on "01-10-2020") (->Account '()))
        (->Account (list {:amount 1000 :local-date (LocalDate/of 2020 10 1) :type :withdraw})))))

  (testing "should withdraw money in an account with some money already"
    (is
      (=
        (withdraw 400 (on "02-10-2020") (->Account (list {:amount 100 :local-date (LocalDate/of 2020 10 1) :type :withdraw})))
        (->Account (list
                     {:amount 100 :local-date (LocalDate/of 2020 10 1) :type :withdraw}
                     {:amount 400 :local-date (LocalDate/of 2020 10 2) :type :withdraw})))))

  (testing "should fail withdrawing money in an account with previous transactions when date is in the past"
    (is
      (=
        (catch-ex-info (deposit 1 (on "01-09-2020") (->Account (list {:amount 1 :local-date (LocalDate/of 2020 10 1) :type :deposit}))))
        {:msg "Non valid transaction date", :data {:type :non-valid-transaction-date}}))))

(deftest calculate-balances-test
  (testing "should calculate all balances for a given account"
    (is
      (=
        (calculate-balances (->Account (list
                                         {:amount 500 :local-date (LocalDate/of 2020 10 1) :type :deposit}
                                         {:amount 100 :local-date (LocalDate/of 2020 10 2) :type :withdraw})))
        (list
          {:amount 500 :local-date (LocalDate/of 2020 10 1) :type :deposit :balance 500}
          {:amount 100 :local-date (LocalDate/of 2020 10 2) :type :withdraw :balance 400})))))

(deftest print-bank-statements-test
  (testing "should print bank statements"
    (is
      (=
        (print-bank-statements return-println (->Account (list
                                                           {:amount 500 :local-date (LocalDate/of 2020 10 1) :type :deposit}
                                                           {:amount 100 :local-date (LocalDate/of 2020 10 2) :type :withdraw})))
        "date || credit || debit || balance\n02/10/2020 || || 100.00 || 400.00\n01/10/2020 || 500.00 || || 500.00\n"))))
