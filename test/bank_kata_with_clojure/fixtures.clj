(ns bank-kata-with-clojure.fixtures
  (:import (clojure.lang ExceptionInfo)))

(defmacro catch-ex-info [fn]
  `(try
     ~fn
     (catch ExceptionInfo e#
       {:msg (ex-message e#) :data (ex-data e#)})))

(defn return-println [msg]
  (with-out-str (println msg)))
