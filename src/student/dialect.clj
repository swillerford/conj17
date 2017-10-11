(ns student.dialect
  (:require [clojure.string :as str]))

(defn canadianize [string]
  (str/replace string #"\.$" ", eh?"))