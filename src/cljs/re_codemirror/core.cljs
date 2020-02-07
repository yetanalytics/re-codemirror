(ns re-codemirror.core
  (:require [reagent.core :as r]
            [cljsjs.codemirror]))

(defn opts
  "Grab the options componenets from the object."
  [this]
  (-> this
      r/argv
      rest
      first))

(defn conf
  "Grab the configuration componenets from the object."
  [this]
  (-> this
      r/argv
      rest
      second))

(defn value
  "Function used to take updates to CodeMirror and reflect them in
   a state manager."
  [this cm _]
  (let [{:keys [value]} (conf this)]
    (when-not (= value (.getValue cm))
      (.setValue cm value))))

(defn codemirror
  []
  (r/create-class
   {:reagent-render      (fn [_ {:keys [name value]}]
                           ;; Render a textarea element and accept a name
                           ;; to use it as a form
                           [:textarea
                            {:name         name
                             :defaultValue value}])
    :component-did-mount (fn [this]
                           (let [opts (opts this)
                                 conf (conf this)
                                 ;; create a CodeMirror object from the textarea
                                 ;; merge in default options with passed opts
                                 cm   (-> this
                                          r/dom-node
                                          (js/CodeMirror.fromTextArea
                                           (clj->js (merge {:mode  "javascript"
                                                            :theme "default"}
                                                           opts))))]
                             ;; add the CM object to the React component so it can be accessed
                             (r/set-state this {:cm cm})
                             ;; attach any optional events to the CodeMirror target
                             (doseq [[event-name event-fn]
                                     (:events conf)]
                               (.on cm
                                    event-name
                                    (fn [& args]
                                      (event-fn this args))))))
    :component-did-update (fn [this old-argv]
                            ;; provide a way for CodeMirror to be updated by external state changes
                            (let [cm (-> this
                                         r/state
                                         :cm)]
                              (value this cm old-argv)))}))
