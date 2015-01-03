(ns mc-site.core.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]])
  (:use mc-site.pokereader.file-upload))

(defroutes app-routes
  (GET "/" [] (clojure.java.io/resource "public/wwwroot/index.html"))
  (GET "/projects" [] (clojure.java.io/resource "public/wwwroot/projects.html"))
  (GET "/pokereader" [] (clojure.java.io/resource "public/wwwroot/pokereader.html"))
  (GET "/pokereader/save/:id" {params :params} (get-save (:id params)))
  (POST "/pokereader/save/file" {params :params} (file-upload (:savefile params)))
  (route/not-found "Not Found"))

(def my-site-defaults
  (assoc site-defaults :security
         (assoc (site-defaults :security) :anti-forgery false)))

(def app
  (wrap-defaults app-routes my-site-defaults))
