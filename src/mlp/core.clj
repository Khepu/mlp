
(ns mlp.core
  (:use [mlp.neural-network]
        [mlp.dataset]
        [mlp.activators]
        [clojure.pprint]
        :reload-all))

(defn dict
  [choice]

  (let [iff #(if (= choice %) 1 0)]
    {"Iris-setosa" (iff 1)
     "Iris-versicolor" (iff 2)
     "Iris-virginica" (iff 3)}))

(defn -main
  [& args]

  (let [[x t-str] (read-dataset "./resources/iris.data")
        t (mapv (dict 1) t-str)]
     (-> {:neurons [2 1]
          :activators [sigmoid sigmoid]
          :x (first x)
          :target (vector (first t))
          :learning-rate 1}
         neural-net
         feed-forward
                                        ;back-propagation
         )))

