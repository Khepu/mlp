(ns mlp.neural-network
  (:use [clojure.core.matrix :only [mmul
                                    transpose]]
        [mlp.activators]
        :reload-all))
(def t transpose)
(def m mmul)

(defn rand-v
  "Returns a vector of size n with random values in range [0, 1] (lazy)"
  [n]
  #(vec (take n (repeatedly rand))))

(defn reversev
  [xs]
  (vec (reverse xs)))

(defn deep-get
  "Extracts the values for a given key from all sub-vectors"
  [network key]
  (mapv #(mapv key %) network))

(defn get-weights
  [network]
  (deep-get network :weights))

(defn get-activation
  [network]
  (deep-get network :activation))

(defn get-sigma-prime
  [network]
  (deep-get network :sigma-prime))

(defn append-bias
  [x]
  #(conj % 1) x)

(defn haddamard-product
  [left right]
  (let [columns (if (vector? (first left))
                  (count (first left))
                  (count left))
        product (partition columns (map * (flatten left) (flatten right)))]
    (if (= (count product) 1)
      (first product)
      product))) ;turned to list from vector!!!

                                        ;;;Neural Network

(defn neuron
  [sigma x w]
  (let [z (mmul w (transpose x))]
    {:weights w
     :activation (sigma z nil)
     :sigma-prime (sigma z :derivative)}))

(defn layer
  [neurons sigma x]
  (let [layer (vec (take neurons (repeatedly (rand-v (count x)))))
        neuron-f (partial neuron sigma x)]
    (map neuron-f layer)))

(defn neural-net
  [params]
  (let [{:keys [neurons
                activators
                x
                learning-rate
                weights]
         :or {neurons nil
              weights nil}
         :as nn} (update params :x append-bias)
        layer-configs (map vector neurons activators)

        layers (map #(partial layer (first %) (last %)) layer-configs)]
    (assoc nn :layers (map #(partial layer (first %) (last %)) layer-configs))))

  (defn feed-forward
    "Feeds a single pattern through the network"
  [network]
  (let [layers (:layers network)
        x (:x network)
        result (reduce #(conj %1 (%2 (mapv :activation (last %1))))
                       [((first layers) x)]
                       (rest layers))]
    (assoc network
           :weights (get-weights result)
           :activation (get-activation result)
           :sigma-prime (get-sigma-prime result))))

(defn delta-calc
  [next-delta w sigma]
  (haddamard-product (mmul next-delta w) sigma))

(defn back-propagation
  [network]
  (let [activation (:activation network)
        weights (:weights network)
        target (:target network)
        columns (count (last activation))
        sigma-primed (:sigma-prime network)
        delta-out (haddamard-product (mapv - (last activation) target) (vector (last sigma-primed)))]
    (loop [w (reverse weights)
           sigma (reverse (butlast sigma-primed))
           alpha (reverse activation)
           delta (list delta-out)]
      (if (empty? sigma)
        delta
        (recur (rest w)
               (rest sigma)
               (rest alpha)
               (conj delta (delta-calc (first delta) (first w) (first sigma))))))))

(defn update-weights
  [network])

(defn mean-squared-error
  "y is the guessed output from the network and t the actual target"
  [y d]
  (let [p (count d)
        dy (mapv vector (flatten d) (flatten y))]
    (/ (reduce #(+ %1 (Math/pow (- (first %2) (last %2)) 2)) 0 dy)
       (* 2 p))))

