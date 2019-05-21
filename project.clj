(defproject mlp "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [plotly-clj "0.1.1"]
                 [org.clojure/data.csv "0.1.3"]
                 [net.mikera/core.matrix "0.57.0"]]
  :plugins [[lein-gorilla "0.4.0"]
            [cider/cider-nrepl "0.21.1"]]
  :main ^:skip-aot mlp.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
