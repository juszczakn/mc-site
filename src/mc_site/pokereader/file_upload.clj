(ns mc-site.pokereader.file-upload
  (:use pokereader.reader)
  (:use hiccup.core)
  (:require [clj-http.client :as http]))

(def pokeapi-base "http://pokeapi.co")
(def pokeapi (str pokeapi-base "/api/v1/"))

(def pokeapi-sprite (str pokeapi "sprite/"))

(defn- poke-entry
  [pokemon]
  (let [{index :index, name :pokemon, level :level} pokemon]
    ;(http/get (str pokeapi-sprite (inc index)))
    [:div {:class "pokemon"}
     [:h2 (str name " Lvl " level)]
     [:ul {:style "align:left;"}
      (for [m (:moves pokemon)]
        (when-let [move (second m)]
          [:li {:style "align:left;"} move]))]]))

(defn- create-layout
  [data]
  [:body
   [:h1 (str "Name: " (:name data))]
   [:hr]
   [:h1 {:style "align:left;"} "Team: "]
   (for [pokemon (:team data)]
     (poke-entry pokemon))])

(defn file-upload
  [file-map]
  (let [size (file-map :size)
        file (file-map :tempfile)
        filename (file-map :filename)
        data (get-save-data file)]
    (html
     [:head
      [:link {:href "../../css/main.css" :rel "stylesheet" :type "text/css"}]
      [:title "PokeReader"]]
     (create-layout data))))
