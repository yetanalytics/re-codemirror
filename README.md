# re-codemirror
A reagent component that wraps [CodeMirror](https://codemirror.net/) and provides entry points for a reactive state manager.

## Usage
To use `re-codemirror` add the git dependency to your project and use the latest sha release.

### Basic

```clojure
(ns ...
  (require [re-codemirror.core :as re-cm]))

;; Reagent Component
(defn editor
  []
  [re-cm/codemirror
   {:lineNumbers true}
   {:name "form-input"}])
```

### Mode

```clojure
(ns ...
  (require ...
           [cljsjs.codemirror.mode.clojure]))

;; CodeMirror component
(defn clojure-editor
  []
  [re-cm/codemirror
   {:mode "clojure"}
   {:name "form-input}])
```

### Linting

```clojure
(ns ...
  (require ...
           [cljsjs.codemirror.mode.javascript]
           [cljsjs.codemirror.addon.lint.javascript-lint]))
           
;; CodeMirror component
(defn javascript-editor
  []
  [re-cm/codemirror
   {:mode    "javascript"
    :gutters ["CodeMirror-link-markers"]
    :lint    true}
   {:name "form-input"}])
```

### Reactive Statement Management

Manage state through [re-frame](https://github.com/yetanalytics/re-frame).

```clojure
(ns ...
  (require ...
           [re-frame.core :refer [subscribe dispatch]]))

;; CodeMirror component
(defn re-frame-editor
  [sub-key dis-key]
  [re-cm/codemirror
   {}
   {:name   "form-input"
    ;; subscribe to re-frame and get a possible text value
    :value  @(subscribe [sub-key])
    ;; This supports a simple use case of just catching when the text value changes.
    ;; Update the full text value from the .getValue function from CodeMirror
    ;; Any event can be added that is supported through CodeMirror in the map
    ;;    "name" (callback-fn ...)
    :events {"change" (fn [this [code-mirror-obj change-obj]]
                        (dispatch [dis-key (.getValue code-mirror-obj)]))}}])
```

You can also manage state through any other library of your choice.
