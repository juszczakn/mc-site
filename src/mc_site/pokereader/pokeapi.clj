(ns mc-site.pokereader.pokeapi
  (:require [clj-http.client :as client]))

(def pokeapi-base "http://pokeapi.co")
(def pokeapi (str pokeapi-base "/api/v1/"))

(def pokeapi-sprite (str pokeapi "sprite/"))

(defn get-sprite
  [index]
  (let [response (:body (client/get (str pokeapi-sprite (inc index))))]
    (str pokeapi-base (re-find #"/media/img/[0-9]+.png" response))))
