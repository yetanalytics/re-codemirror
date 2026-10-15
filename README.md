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

## Dependency vulnerability tracking

The `Deps` workflow regenerates `pom.xml` from `deps.edn` and submits the Maven
dependency tree to GitHub's dependency graph on pushes to `master`. It can also
be run manually. Dependabot uses this graph to check for known vulnerabilities.

To regenerate the POM locally, run:

```sh
clojure -X:deps mvn-pom :user nil
```

This excludes local user dependency configuration while retaining the POM's
project metadata. Since `deps.edn` does not pin Clojure itself, the generated POM
also includes the Clojure CLI's default Clojure dependency.
The snapshot is associated with `deps.edn` and includes ClojureScript, Reagent,
the CLJSJS CodeMirror package, and their transitive Maven dependencies.

The CLJSJS package is submitted under its Maven coordinates. This does not
establish npm advisory coverage for the JavaScript bundled inside that package;
upstream JavaScript dependencies require separate coverage review.
