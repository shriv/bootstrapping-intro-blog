;; gorilla-repl.fileformat = 1

;; **
;;; # Bootstrapping: an introduction with Clojure
;;; 
;; **

;; @@
(ns bootstrapping-blog-post	
  (:require [gorilla-plot.core :as plot]
            [clojure.java.io :as io]
            [incanter.core :as i]
            [incanter.io :as incio]
            [incanter.datasets :as dsets])
  (:use [incanter-gorilla.render]))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}
;; <=

;; **
;;; ### A typical (and simple) analysis
;;; You have some numeric data and you need to report its mean with some degree of confidence. Let's start with the well-known iris dataset [ref 1]. We'll use the iris dataset available in the Incanter statistical analysis library to obtain the mean petal length (and errors) for Iris Setosa.
;; **

;; **
;;; #### Get the iris dataset
;; **

;; @@
(def iris (dsets/get-dataset :iris))
(i/head 2 iris)
;; @@
;; =>
;;; {"type":"list-like","open":"<span class='clj-map'>{</span>","close":"<span class='clj-map'>}</span>","separator":", ","items":[{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:column-names</span>","value":":column-names"},{"type":"list-like","open":"<span class='clj-vector'>[</span>","close":"<span class='clj-vector'>]</span>","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:Sepal.Length</span>","value":":Sepal.Length"},{"type":"html","content":"<span class='clj-keyword'>:Sepal.Width</span>","value":":Sepal.Width"},{"type":"html","content":"<span class='clj-keyword'>:Petal.Length</span>","value":":Petal.Length"},{"type":"html","content":"<span class='clj-keyword'>:Petal.Width</span>","value":":Petal.Width"},{"type":"html","content":"<span class='clj-keyword'>:Species</span>","value":":Species"}],"value":"[:Sepal.Length :Sepal.Width :Petal.Length :Petal.Width :Species]"}],"value":"[:column-names [:Sepal.Length :Sepal.Width :Petal.Length :Petal.Width :Species]]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:rows</span>","value":":rows"},{"type":"list-like","open":"<span class='clj-lazy-seq'>(</span>","close":"<span class='clj-lazy-seq'>)</span>","separator":" ","items":[{"type":"list-like","open":"<span class='clj-map'>{</span>","close":"<span class='clj-map'>}</span>","separator":", ","items":[{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:Species</span>","value":":Species"},{"type":"html","content":"<span class='clj-string'>&quot;setosa&quot;</span>","value":"\"setosa\""}],"value":"[:Species \"setosa\"]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:Petal.Width</span>","value":":Petal.Width"},{"type":"html","content":"<span class='clj-double'>0.2</span>","value":"0.2"}],"value":"[:Petal.Width 0.2]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:Petal.Length</span>","value":":Petal.Length"},{"type":"html","content":"<span class='clj-double'>1.4</span>","value":"1.4"}],"value":"[:Petal.Length 1.4]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:Sepal.Width</span>","value":":Sepal.Width"},{"type":"html","content":"<span class='clj-double'>3.5</span>","value":"3.5"}],"value":"[:Sepal.Width 3.5]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:Sepal.Length</span>","value":":Sepal.Length"},{"type":"html","content":"<span class='clj-double'>5.1</span>","value":"5.1"}],"value":"[:Sepal.Length 5.1]"}],"value":"{:Species \"setosa\", :Petal.Width 0.2, :Petal.Length 1.4, :Sepal.Width 3.5, :Sepal.Length 5.1}"},{"type":"list-like","open":"<span class='clj-map'>{</span>","close":"<span class='clj-map'>}</span>","separator":", ","items":[{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:Species</span>","value":":Species"},{"type":"html","content":"<span class='clj-string'>&quot;setosa&quot;</span>","value":"\"setosa\""}],"value":"[:Species \"setosa\"]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:Petal.Width</span>","value":":Petal.Width"},{"type":"html","content":"<span class='clj-double'>0.2</span>","value":"0.2"}],"value":"[:Petal.Width 0.2]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:Petal.Length</span>","value":":Petal.Length"},{"type":"html","content":"<span class='clj-double'>1.4</span>","value":"1.4"}],"value":"[:Petal.Length 1.4]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:Sepal.Width</span>","value":":Sepal.Width"},{"type":"html","content":"<span class='clj-double'>3.0</span>","value":"3.0"}],"value":"[:Sepal.Width 3.0]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:Sepal.Length</span>","value":":Sepal.Length"},{"type":"html","content":"<span class='clj-double'>4.9</span>","value":"4.9"}],"value":"[:Sepal.Length 4.9]"}],"value":"{:Species \"setosa\", :Petal.Width 0.2, :Petal.Length 1.4, :Sepal.Width 3.0, :Sepal.Length 4.9}"}],"value":"({:Species \"setosa\", :Petal.Width 0.2, :Petal.Length 1.4, :Sepal.Width 3.5, :Sepal.Length 5.1} {:Species \"setosa\", :Petal.Width 0.2, :Petal.Length 1.4, :Sepal.Width 3.0, :Sepal.Length 4.9})"}],"value":"[:rows ({:Species \"setosa\", :Petal.Width 0.2, :Petal.Length 1.4, :Sepal.Width 3.5, :Sepal.Length 5.1} {:Species \"setosa\", :Petal.Width 0.2, :Petal.Length 1.4, :Sepal.Width 3.0, :Sepal.Length 4.9})]"}],"value":"\n| :Sepal.Length | :Sepal.Width | :Petal.Length | :Petal.Width | :Species |\n|---------------+--------------+---------------+--------------+----------|\n|           5.1 |          3.5 |           1.4 |          0.2 |   setosa |\n|           4.9 |          3.0 |           1.4 |          0.2 |   setosa |\n"}
;; <=

;; **
;;; #### Get the mean and standard error of the mean for petal length of Iris Setosa
;;; 
;; **

;; @@
;; Functions ;;
(defn mean-function
  [values]
  (/ (apply + values) (count values)))

(defn standard-err
  [values]
  (/ (apply + values) (count values)))

;; Filter relevant data ;;
(def setosa (i/query-dataset iris {:Species {:$eq "setosa"}}))
(def setosa-petal-length (flatten (i/to-vect (i/sel setosa :cols [:Petal.Length]))))

;; Apply functions to data ;;
(mean-function setosa-petal-length)
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-double'>1.4620000000000002</span>","value":"1.4620000000000002"}
;; <=

;; @@
(with-open [rdr (io/reader "/Users/shrividya/Documents/bootstrapping-blog-post/iris-petal-length-setosa.csv")]
    (doseq [line (line-seq rdr)]
    (println line)))
;; @@
;; ->
;;; 1.4
;;; 1.4
;;; 1.3
;;; 1.5
;;; 1.4
;;; 1.7
;;; 1.4
;;; 1.5
;;; 1.4
;;; 1.5
;;; 1.5
;;; 1.6
;;; 1.4
;;; 1.1
;;; 1.2
;;; 1.5
;;; 1.3
;;; 1.4
;;; 1.7
;;; 1.5
;;; 1.7
;;; 1.5
;;; 1
;;; 1.7
;;; 1.9
;;; 1.6
;;; 1.6
;;; 1.5
;;; 1.4
;;; 1.6
;;; 1.6
;;; 1.5
;;; 1.5
;;; 1.4
;;; 1.5
;;; 1.2
;;; 1.3
;;; 1.4
;;; 1.3
;;; 1.5
;;; 1.3
;;; 1.3
;;; 1.3
;;; 1.6
;;; 1.9
;;; 1.4
;;; 1.6
;;; 1.4
;;; 1.5
;;; 1.4
;;; 
;; <-
;; =>
;;; {"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}
;; <=

;; **
;;; ### A typical (and more difficult) analysis
;; **

;; **
;;; ### 
;; **

;; @@

;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}
;; <=

;; **
;;; ### Bootstrapping in clojure
;; **

;; **
;;; #### The bootstrap method
;; **

;; **
;;; #### Applying bootstrapping to data
;; **

;; **
;;; ##### Define a metric of interest
;; **

;; **
;;; ##### Determine the number of sample to convergence
;; **

;; **
;;; #### Bootstrapped errors and confidence intervals
;; **

;; **
;;; ### Conclusions
;; **

;; @@

;; @@
