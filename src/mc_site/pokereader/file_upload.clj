(ns mc-site.pokereader.file-upload
  (:use pokereader.reader)
  (:require [mc-site.pokereader.http :as http]))

(def saves-map-file "pokemon-saves-map.txt")
(def saves-hash-file "pokemon-saves-hash.txt")

(def uploaded-save-hash (try
                          (atom (read-string (slurp saves-hash-file)))
                          (catch Exception e (do (println e) (atom 0)))))
(def uploaded-saves (try
                      (atom (read-string (slurp saves-map-file)))
                      (catch Exception e (do (println e) (atom {})))))

(defn- save-to-file
  []
  (binding [*print-dup* true]
    (spit saves-map-file @uploaded-saves)
    (spit saves-hash-file @uploaded-save-hash)))

(.. (Runtime/getRuntime) (addShutdownHook (proxy [Thread] []
                                            (run []
                                              (save-to-file)))))
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
  (try
    (let [key (Integer/parseInt key)
          data (@uploaded-saves (num key))]
      (http/upload-page data key))
    (catch Exception e (println e))))
