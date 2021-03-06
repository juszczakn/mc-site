(ns mc-site.pokereader.http
  (:use mc-site.pokereader.pokeapi
        hiccup.core))

(def stats-list '("attack" "defense" "speed" "special"))
(def evs-list (cons "hp" stats-list))

(defn- get-evs
  [evs]
  [:table {:border 1}
   [:tr [:th "ev"] [:th "value"]]
   (for [ev evs-list]
     [:tr [:td ev] [:td ((keyword ev) evs)]])])

(defn- get-stats
  [stats]
  [:table {:border 1}
   [:tr [:th "stat"] [:th "value"]]
   (for [stat stats-list]
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
   [:h1 "Link to share: " [:a {:href (str "/pokereader/save/" key)} (str "/save/" key)]]
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
