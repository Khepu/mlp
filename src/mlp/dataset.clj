(ns mlp.dataset
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]))

(defn to-double
  "Converts string to double"
	[str]
	(Double/parseDouble str))

(defn read-dataset
  "reads a csv file and returns 2 vectors. x and t"
  [path]
  (with-open [reader (io/reader path)]
    (let [dataset (doall (csv/read-csv reader))
          x (mapv (comp (partial mapv to-double) butlast) dataset)
          t (mapv (comp last) dataset)]
      [x t])))

