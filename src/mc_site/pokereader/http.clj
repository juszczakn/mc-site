(ns mc-site.pokereader.http
  (:use mc-site.pokereader.pokeapi
        hiccup.core))

(def stats '("attack" "defense" "speed" "special"))
(def evs (cons "hp" *stats*))

(defn- get-evs
  [evs]
  [:table {:border 1}
   [:tr [:th "ev"] [:th "value"]]
   (for [ev *evs*]
     [:tr [:td ev] [:td ((keyword ev) evs)]])])

(defn- get-stats
  [stats]
  [:table {:border 1}
   [:tr [:th "stat"] [:th "value"]]
   (for [stat *stats*]
     [:tr [:td stat] [:td ((keyword stat) stats)]])])

(defn- get-moves
  [moves]
  [:table {:border 1}
   [:tr [:th "move"] [:th "pp"]]
   (for [i (range 1 5)]
     (let [mk (keyword (str "move-" i))
           pk (keyword (str "move-" i "-pp"))]
       (when-let [move (second (mk moves))]
         [:tr [:td move] [:td (pk moves)]])))])

(defn- poke-entry
  [pokemon]
  (let [{index :index, name :pokemon, level :level} pokemon]
    [:div {:class "pokemon"}
     [:br]
     [:hr]
     [:h2 (str name " Lvl " level)]
     [:div 
      [:h4 (str "Type1/" (second (:type-1 pokemon)))]
      [:h4 (str "Type2/" (second (:type-2 pokemon)))]
      [:h4 (str "Max HP: " (:max-hp pokemon))]
      [:img {:src (get-sprite index) :class "sprite"}]]
     (get-stats (:stats pokemon))
     (get-moves (:moves pokemon))
     (get-evs (:evs pokemon))]))

(defn- create-layout
  [data key]
  [:body
   [:h1 (str "Link to share: /save/" key)]
   [:h1 (str "Name: " (:name data))]
   [:h1 {:style "align:left;"} "Team: "]
   (for [pokemon (:team data)]
     (poke-entry pokemon))])

(defn upload-page
  [data key]
  (html
   [:head
    [:link {:href "../../css/main.css" :rel "stylesheet" :type "text/css"}]
    [:link {:href "../../css/pokereader.css" :rel "stylesheet" :type "text/css"}]
    [:title "PokeReader"]]
   (create-layout data key)))
