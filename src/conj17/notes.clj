;; Notes!

;; Commas are whitespace! You can use then for clarity, or not

;; DATA STRUCTURES
(+ 3 4)
;; ( = invocation, list
;; + = function, symbol
;; 3 4 = arguments, numbers

;; NOTE that a list and a function expression are pretty much the exact same thing - the only difference is that Clojure notices the first element is an invocable fn

;; Operation form
;; (op ...)
;; op can be a fn, macro, etc

;; Function calls ALWAYS use parens
(defn hello [] "Hello, world!")
hello ;; returns the fn
(hello) ;; invokes the fn

;; Values NEVER use parens
(def my-name "sean") ;; my-name is a value
my-name ;; return the value
(my-name) ;; Invoke the value - will fail! ClassCastException java.lang.String cannot be cast to clojure.lang.IFn  user/eval1241 (form-init9026520885785175873.clj:1)

;; PREVENTING evaluation
;; You can return an un-evaluated expr
(quote (+ x 2))
;;=> (+ x 2)
;; You can also use ' instead of quote
'(+ x 2)
;;=> (+ x 2)

;; clojuredocs.org

;; find-doc - finds all documentation with a certain string
(find-doc "function")

;; apropos - a way to find fns with a partial match in the repl
(apropos "function")

;; source - prints out the source code of a fn!
(source not)
;;(defn not
;;  "Returns true if x is logical false, false otherwise."
;;  {:tag Boolean
;;   :added "1.0"
;;   :static true}
;;  [x] (if x false true))
;;=> nil

;; dir - prints out all fns available in your repl

;; Clojure libraries - check Clojars!

;; defn
;;    name  params  body
;;    ----- ------  ------------------
(defn greet [name] (str "Hello, " name))

;; multi-arity
;; Each arity is a list ([args] body)
;; Arities can invoke one another
(defn messenger
  ([]                           ; no args
   (messenger "Hello world!"))  ; call self with default
  ([msg]
   (println msg)))

;; variadic fns can have any number of arguments
;; Uses rest param - &
;; Remaining arguments represented as a seq
(defn messenger [greeting & who]
  (println greeting who))
(messenger "Hello" "world" "class")
;;=> Hello (world class)

;; defn is just a shorcut for (def name (fn ...))

;; Function reader macro
;; Terse form #() for short fns
;; Single argument: %
;; Multiple args: %1, %2, %3...
;; Variadic: %&
;; Can't nest with this

;; apply
;; invokes fns on arguments
;; "unpacks" remaining arguments in a seq
(f 1 2 3 4)
;; is equivalent to
(apply f '(1 2 3 4))
(apply f 1 '(2 3 4))
;; Can help using variadic fns to unpack rest parameters

;; partial
;; partially applies a fn with a subset of the args
;; returns a new fn that takes remaining args and invokes with apply

;; let
;; let allows you to define a local scope for a fn
(defn messenger [msg]
  (let [a 7
        b 5
        c (capitalize msg)]
    (println a b c)
    ) ; end of let scope
  ) ; end of fn

;;  closures
;; fn creates a "closure" - closes over surrounding lexical scope

;; You can invoke Java code from clojure! Called "interop"
;; rnd.nextInt() ->
(.nextInt rnd)
;; Java methods are not clojure fns - can't be passed, etc.
;; You can wrap it in a clj fn, however

;; FLOW CONTROL
;; in Clojure everything is an expression and returns a value
;; A block of multiple expressions returns the last value (e.g. let, do, fn)
;; Expressions for side effects (e.g. println) return nil

;; Flow control operators are expressions too!

;; IF
(if (:boolean-expression) :if-true :if-false)

;; TRUTHINESS!
;; Boolean true is truthy
;; Objects are truthy
;; Empty collections are truthy
;; Zero is truthy
;; THE ONLY THINGS that are falsey are false and nil
;; Note that (seq []) returns nil, so (seq []) is falsey

;; To use multiple expressions per branch, use if-do
(if (even? 5)
  (do (println "even")
      true)
  (do (println "odd")
      false))

;; when
;; If cond true, execute body, else return nil

;; cond
;; Series of tests and expressions; :else is optional
(cond
  test1 exp1
  test2 exp2
  ...
  :else expelse)

;; case evaluates whether argument is the same as a value
(defn foo [x]
  (case x
    5 "x is 5"
    10 "x is 10"
    "x isn't 5 or 10"))

;; dotimes - used for side effects
;; Evaluates expression n times, returns nil
(dotimes [i 3]
  (println i))
;;=> 0
;;=> 1
;;=> 2

;; doseq - iterates over a seq, similar to java for-each
;; Can use with multiple bindings - processes all permutations, similar to nested loop
;; Used for side effects

;; for
;; is used for list comprehension, NOT looping
;; Bindings behave like doseq

;; RECURSION
;; loop and recur
;; loop defines binding
;; recur re-executes loop with new bindings
(loop [i 0]
  (if (< i 10)
    (recur (inc i))
    i))
;; fn arguments are implicit loop bindings - recur will put you back at defn or fn
;; recursion via recur doesn't consume stack, vs calling the fn explicitly
;; recur must be in "tail position" - last expr in branch

;; EXCEPTIONS!
;; try/catch/finally like Java
(try
  (/ 2 1)
  (catch ArithmeticException e
    "divide by zero")
  (finally
    (println "cleanup")))

(try
  (throw (Exception. "something went wrong")) ;; Exception. is creating new instance
  (catch Exception e (.getMessage e)))

;; ex-info takes a message and a map - exception message + place to store exception data
;; ex-data gets the map back out if there is one


;; COLLECTIONS!
;; Immutability - cannot "overwrite" or "update in place" in clojure
;; This means no unexpected changes
;; Simple values are immutable - compound values are immutable too.
;; Choose your collection type by the operations it supports efficiently!

;; Sets have no order (but you can make a sorted set)
;; Sets have NO DUPLICATES!
#{"this" "is" "a" "set"}
;; Uses: just need a coll, need to check if coll has an item, need to efficiently add/remove items

(def players #{"Alice" "Bob" "Kelly"})
(conj players "Una") => #{"Alice" "Bob" "Una" "Kelly"}
;; because of immutability, players is unchanged
;; contains? only works on sets, not lists or vectors

;; Sets are functions!
(def s #{1 2 3})
(s 7)
;;=> 7
(s 20)
;;=> nil

;; Sorted sets preserve "natural" sort order of items - uses clojure's compare
;; construct by (sorted-set [&args])
;; Sorted set can be provided with your OWN compare function
(sorted-set-by compare-fn [&args])
;; Note - if comparator says elements are equal, one will be removed!

;; Vectors
;; Ordered, indexed, insertion point is the end
;; use vector to preserve order and efficiently get item at position n
(def callsigns ["alfa" "bravo" "charlie"])
(nth callsigns 2)
;;=> "charlie"
;; nth gets nth item of vector
;; Throws error if index is out of bounds

(peek callsigns) ;; gets last item
;;=> "Charlie"
;; peek is waaaay more efficient than last
(pop callsigns) ;; gets everything but last item
;;=> ["alfa" "bravo]
;; You can get item from vector by using it as a fn
(callsigns 1)
;;=> Bravo

;; Lists
;; Must quote in code - '(1 2 3)
;; NOT INDEXED
;; Use if you need efficient access to first element, or have a stack (last in, first out)
;; first gets first item
;; rest gets all but first item
;; peek/pop ~= first/rest

;; into allows you to combine colls
;; coll takes the type of the first arg - you can pour a vector into a set, for instance

;; DESTRUCTURING
;; Declarative way to pull apart compound data, vs explicit, verbose access
;; Destructuring nests for deep, arbitrary access inside of colls
;; you can provide a vector of symbols to bind by position
(def stuff [7 8 9 10 11])

;; bind a, b, c to first 3 vals
(let [[a b c] stuff]
  (list (+ a b) (+ b c)))
;;=> (15 17)
;; binds to nil if no data
(let [[a b c d e f] stuff]
  (list d e f))
;;=> (10 11 nil)

;; You can use a rest symbol (&) to do destructuring
(let [[a & others] stuff]
  (println a)
  (println others))
;; 7
;; [8 9 10 11]

;; CHOOSING COLLS
;; Each data structure op has specific performance guarantees
;; Most ops are either linear or constant
;; If you're working with small collections, performance won't matter much. For something like the FME, it matters a LOT

;; Colls as indices - maps!
;; use get to get by key
(get map-name :key "default return value")

;; maps are fns of their keys
(def m {:a 1 :b 2})
(m :b) ;;=> 2
(m :foo) ;;=> nil
(m :foo 50) ;;=> 50
;; keys are fns of their maps
(:a m) ;;=> 1

;; contains? works on maps as well
;; returns key/value

(keys map-name) ;; returns list of keys
(vals map-name) ;; returns list of values

;; zipmap lets you turn a set into a map
(zipmap #{:a :b :c} (repeat 0))
;;=> {:a 0 :b 0 :c 0}

;; merge combines maps (any number of them)
;; If any maps contain same key, the last map wins
;; If you want to manage this, use merge-with and provide a fn to handle duplicates
(merge-with + scores new-scores) ;; will add scores together where key is identical

;; sorted-map maintains keys in natural sort order
;; Uses clojure's compare
;; keys and vals return in sorted order
;; you can do your own comparator - sorted-map-by
;; sorts by keys only

;; Map destructuring!
;; {} as left side of binding initiates map destructuring
;; {new :old} extracts :old and binds to new
;; {:keys [a]} extracts :a and binds as a
;; {:syms [a b]} extracts a
;; {:strs [a b]} extracts "a"
;; :or lets you provide a default value
;; :keys gets keywords, :syms gets symbols, :strs gets strings

;; Applying a vector of keys to an & binding emulates named args

;; TRANSIENTS
;; Very performative
;; Rapidly accumulating values, using intermediate steps,
;; Create one from existing coll with transient fn
;; Transient coll supports "read" methods
;; Write methods need to have ! - conj! instead of conj
;; Turn back into persistent with persistent!

;; Thread-first
(get (get company :address) :city)
(-> company :address :city) ;; same as above

;; RECORDS!
;; Records have better performance and a named type
;; polymorphic with protocols
;; Used like maps, but can't be invoked like functions
(defrecord Person [first-name last-name age occupation]) ;; defining a record type
;; Gives you a positional constructor
(def kelly (->Person "Kelly" "Keen" 32 "Programmer"))
;; You can also give it a map
(def kelly (map->Person
             {:first-name "Kelly"
              :last-name "Keen"
              :age 32
              :occupation "Programmer"}))

;; CONSTRUCTING
;; You can construct things a few ways - with a literal, with a constructor, or as a result as another collection.

;; SEQUENCES
;; Sequences aren't really a data structure. A list is a seq, but a seq isn't necessarily a list
;; A seq is a source of values, in some order
;; Vector/list: collection order
;; Map or set: arbitrary, but consistent
;; Sorted map/set: in sort order
;; Seqs may be lazy

;; Partial computation / laziness
;; Seq operations can be put into a stack, and consumed lazily/non-lazily
;; (seq) gets a seq from any data structure - seq on empty coll returns nil (nil is falsey)

;; map is really handy for sequences!

;; DON'T MIX SIDE EFFECTS AND LAZINESS

;; Use side effects at the end, usually with doseq (sometimes reduce)
;; Sometimes, doall or dorun to force eval of lazy seq

;; Many seq ops have non-lazy equivalents that return vectors
;; map = mapv
;; filter = filterv
;; concat = into []
;; last = peek
;; butlast = pop

;; You can use seq functions on ANYthing that is a seq - strings, vectors, lists, maps, etc...


;; TRANSDUCERS
;; Transformations can be applied in many contexts other than just map and filter
;; Transducers vs seqs
;; Creating a transformation once & applying in multiple contexts improves code factoring, performance, etc.
;; Transducers are eager not lazy
;; Transducers allow you to collapse a series of transformation steps without going thru a bunch of intermediate steps.
;; See the graphic here: https://medium.com/@roman01la/understanding-transducers-in-javascript-3500d3bd9624
;; Process items one by one into a single output array

;; Most seq fns have reduced arity form to create a transducer
(map inc) ;; notice no seq here - this is a transducer

;; Combine transducers with comp - composition creates modified recipe
;; transducer example:
(comp (filter odd?) (map inc) (partition-all 2))
;; INPUT -> filter out even numbers -> increment by one -> partition -> OUTPUT

;; transduce fn is like reduce, but with a transducer. Applies to an input coll and accumulates an output
;; into collects results of applying transducer to coll
;; sequence can apply a transducer
;; eduction creates a new transducer every time - no caching




