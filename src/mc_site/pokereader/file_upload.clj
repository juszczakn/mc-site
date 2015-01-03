(ns mc-site.pokereader.file-upload
  (:use pokereader.reader)
  (:require [mc-site.pokereader.http :as http]))

(def uploaded-save-hash (atom 0))
(def uploaded-saves (atom {}))

(defn file-upload
  [file-map]
  (let [size (file-map :size)
        file (file-map :tempfile)
        filename (file-map :filename)
        data (get-save-data file)]
    (try
      (let [key (swap! uploaded-save-hash inc)]
        (swap! uploaded-saves assoc key data)
        (http/upload-page data key))
      (catch Exception e (println (str e))))))

(defn get-save
  [key]
  (println key)
  (try
    (let [key (Integer/parseInt key)
          data (@uploaded-saves (num key))]
      (http/upload-page data key))
    (catch Exception e (println e))))
