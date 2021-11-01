(ns bank-kata-with-clojure.account
  (:import [java.time LocalDate]
           (java.time.format DateTimeFormatter)))

(defrecord Account [transactions])

(def non-valid-transaction-date-exception (ex-info "Non valid transaction date" {:type :non-valid-transaction-date}))

(def invalid-amount-exception (ex-info "Invalid amount" {:type :invalid-amount}))

(defn- valid-next-date? [new-date transactions]
  (let [last-date (last (map #(:local-date %) transactions))]
    (if (some? last-date)
      (.isBefore last-date new-date)
      true)))

(defn- add [transaction account]
  (assoc account :transactions (concat (:transactions account) (list transaction))))

(defn- move-money [amount local-date type account]
  (cond
    (< amount 0) (throw invalid-amount-exception)
    (not (valid-next-date? local-date (:transactions account))) (throw non-valid-transaction-date-exception)
    :else (add {:amount amount :local-date local-date :type type} account)))

(defn on
  ([local-date-as-string] (LocalDate/parse local-date-as-string (DateTimeFormatter/ofPattern "dd-MM-yyyy")))
  ([local-date-as-string pattern] (LocalDate/parse local-date-as-string (DateTimeFormatter/ofPattern pattern))))

(defn deposit [amount local-date account]
  (move-money amount local-date :deposit account))

(defn withdraw [amount local-date account]
  (move-money amount local-date :withdraw account))

(defn calculate-balances [account]
  (reduce (fn [acc, current]
            (concat
              acc
              (list (assoc
                      current
                      :balance (cond
                                 (empty? acc) (:amount current)
                                 (= :deposit (:type current)) (+ (:amount current) (:balance (last acc)))
                                 :else (- (:balance (last acc)) (:amount current)))))))
          '()
          (:transactions account)))

(defn default-format-date [local-date]
  (.format (DateTimeFormatter/ofPattern "dd/MM/yyyy") local-date))

(defn default-format-amount [amount]
  (format "%.2f" (double amount)))

(defn print-bank-statements [print-fn account
                             & {:keys [format-date format-amount]
                                :or {format-date default-format-date format-amount default-format-amount}}]
  (let [headers "date || credit || debit || balance" fd format-date fa format-amount]
    (->> (calculate-balances account)
         (reverse)
         (map (fn [transaction]
                (if (= :deposit (:type transaction))
                  (str (fd (:local-date transaction)) " || " (fa (:amount transaction)) " || || " (fa (:balance transaction)))
                  (str (fd (:local-date transaction)) " || || " (fa (:amount transaction)) " || " (fa (:balance transaction))))))
         (clojure.string/join "\n")
         (str headers "\n")
         (print-fn))))
