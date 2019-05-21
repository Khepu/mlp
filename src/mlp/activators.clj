(ns mlp.activators)

(defn sigmoid
  [x derivative?]

  (let [sigma (/ 1 (+ 1 (Math/exp (- x))))]
    (if derivative?
      (* sigma (- 1 sigma))
      sigma)))

(defn relu
  [x derivative?]

  (if derivative?
    (if (> x 0)
      1
      0)
    (max 0 x)))

