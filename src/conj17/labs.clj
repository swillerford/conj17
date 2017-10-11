(ns conj17.labs)


;; LAB: Functions
(defn greet
  "Prints hello"
  []
  (println "Hello!"))

(def greet2 (fn [] (println "Hello!")))
(def greet3 #(println "Hello!"))

(defn greeting
  ([]
    "Hello, World!")
  ([x]
   (str "Hello, " x "!"))
  ([x y]
   (str x ", " y "!")))

(defn do-nothing [x] x)

(defn always-thing [& args] :thing)

(defn make-thingy [x]
  (fn [& args] x))

(defn triplicate [x]
  (x) (x) (x))

(defn opposite [f]
  (fn [& args] (not (apply f args))))

(defn triplicate2 [f & args]
  (triplicate (fn [] apply f args)))

(defn sincos [x]
  (+ (Math/pow (Math/sin x) 2) (Math/pow (Math/cos x) 2)))

(defn gofetch [url]
  (slurp (.openStream (java.net.URL. url))))

;;(defn sample-app [])


;; LAB: Flow Control
(defn check-guess [secret guess]
  (if (= secret guess)
    "You win!"
    (if (< guess secret)
      "Too low"
      "Too high")))

(defn check-guess2 [secret guess]
  (cond
    (= guess secret) "You win!"
    (< guess secret) "Too low"
    (> guess secret) "Too high"))

(defn triplicate [f]
  (dotimes [i 3] (f))) ;; f is in parens b/c we want to execute it

(defn numbers [n]
  (dotimes [i n] (println i)))

(defn counting [n]
  (doseq [i (range n)]
    (println (+ i 1))))

(defn print-bands [guitars basses drums]
  (doseq [guitar guitars bass basses drum drums]
    (println guitar bass drum)))

(defn all-bands [guitars basses drums]
  (for [g guitars
        b basses
        d drums]
    [g b d]))

(defn fizzbuzz []
  (doseq [i (range 100)]
    (cond
      (and (zero? (rem i 3)) (zero? (rem i 5))) (println "FizzBuzz!")
      (zero? (rem i 3)) (println "Fizz!")
      (zero? (rem i 5)) (println "Buzz!")
      :else (println i))))

(defn gcd [a b]
  (cond
    (= a 0) b
    (= b 0) a
    (> a b) (recur (- a b) b)
    :else (recur a (- b a))))

(def scores {"Una" 1400, "Bob" 1240, "Cid" 1024})

(def game
  {:round 2
   :players #{{:name "Una" :ranking 43}
              {:name "Bob" :ranking 77}
              {:name "Cid" :ranking 33}}
   :scores {"Una" 1400
            "Bob" 1240
            "Cid" 1024}})

(defn next-round [game]
  (update-in game [:round] inc))

(defn add-score [name add game]
  (update-in game [:scores name] + add))

(defn add-player [name ranking game]
  (update-in
    (update-in game [:players] conj {:name name :ranking ranking})
    [:scores] assoc name 0))

(def game2
  {:round 2
   :players {"Una" {:name "Una" :ranking 43 :score 1400}
             "Bob" {:name "Bob" :ranking 77 :score 1240}
             "Cid" {:name "Cid" :ranking 33 :score 1024}}})

(defn find-player [name game]
  (get-in game [:players name]))

(defn remove-player-bad [name game]
  (assoc game :players (dissoc (:players game) name)))

(defn remove-player [name game]
  (update-in game [:players] dissoc name))

(defrecord Course [course-id name prereqs hours])
(defrecord Faculty [faculty-id first last])
(defrecord Offering [offering-id course teacher days start-time end-time])


(defn add-course [course catalog]
  (assoc-in catalog [:courses (:course-id course)] course))

(defn add-faculty [catalog faculty]
  (assoc-in catalog [:faculty (:faculty-id faculty)] faculty))

(defn add-offering [catalog offering]
  (assoc-in catalog [:offerings (:offering-id offering)] offering))

;; Use a let here instead, and maybe also destructuring
(defn offer->str [catalog offering-id]
  (let [offering (get-in catalog [:offerings offering-id])
        course (get-in catalog [:courses (:course offering)])
        faculty (get-in catalog [:faculty (:teacher offering)])]
  (println (:course offering)
           (:name course)
           (str "(" (:hours course) " hrs)"))
  (println (reduce str (:days offering))
           (str (:start-time offering) "-" (:end-time offering))
           (:first faculty)
           (:last faculty))
  (println "Prereqs:" (if (:prereqs course) (clojure.string/join ", " (:prereqs course)) "none"))))


;; Sums and Ciphers lab
(def fibs
  (map first
       (iterate (fn [[a b]] [b (+ a b)])
                [0 1])))

(reduce + (take 50 fibs))

(def primes
  (letfn [(next-prime [known-primes n]
            (lazy-seq
              (if (some #(zero? (rem n %)) known-primes)
                (next-prime known-primes (inc n))
                (cons n (next-prime (conj known-primes n) (inc n))))))]
    (next-prime [] 2)))

;;(reduce + (take 50 (drop-while #(<= % 100))))

(defn rotate [coll n]
  (take (count coll) (drop n (cycle coll))))

(def letters (map char (range 65 (+ 65 26))))

(defn rot13-one-char [letter]
  (letters letter letter))


(defn encipher [s]
  (apply str (rotate s 13)))

;;; RoShamBo
(def dominates
  {:rock :paper
   :paper :scissors
   :scissors :rock})

(def choices (vec (keys dominates)))

(defn winner [c1 c2]
  (cond
    (= c1 c2) nil
    (= c1 (dominates c2)) c1
    :else c2))

(defn draw? [c1 c2]
  (nil? (winner c1 c2)))

(defn iwon? [c1 c2]
  (= c1 (winner c1 c2)))

(defprotocol Player
  (choose [p])
  (update-player [p me you]))

(defrecord Random []
  Player
  (choose [_] (rand-nth choices))
  (update-player [this me you] this))

(defrecord Stubborn [c]
  Player
  (choose [_] c)
  (update-player [this me you] this))

(defrecord Mean [last-winner]
  Player
  (choose [_] (if last-winner last-winner (rand-nth choices)))
  (update-player [_ me you](->Mean (if (iwon? me you) me))))

;; CONCURRENT SURGERY LAB
(defn patient []
  (atom {:arms 2
         :legs 2
         :heads 1}))

(defn init-patients []
  (vec (repeatedly 1000 patient)))

(defn surgeon [specialty]
  {:specialty specialty})

(defn init-surgeons []
  (vec (take 100 (map surgeon (cycle [:arms :legs :heads])))))

(def all-patients nil)
(def all-surgeons nil)

(defn init! []
  (alter-var-root #'all-patients (constantly (init-patients)))
  (alter-var-root #'all-surgeons (constantly (init-surgeons))))

(defn totals []
  (reduce (partial merge-with +)
          (map deref all-patients)))

(defn transplant!
  "a fn for transplanting body parts based on a surgeon's specialty"
  [surgeon donor recipient]
  (let [specialty (:specialty surgeon)
        limbs (get @donor specialty)]
    (when (pos? limbs)
      (swap! donor update-in [specialty] dec)
      (swap! recipient update-in [specialty] inc))))

(defn select-patients []
  (let [n1 (rand (count all-patients))
        n2 (mod (inc n1) (count all-patients))]
    [(nth all-patients n1) (nth all-patients n2)]))

(defn operate! []
  (let [surgn (rand-nth all-surgeons)
        [p1 p2] (select-patients)]
    (transplant! surgn p1 p2)))

(defn run! []
  (init!)
  (dotimes [n 1000]
    (operate!))
  (totals))

(defn operating-room []
  (future (dotimes [_ 1000] operate!)))

(defn run5! []
  (init!)
  (let [rooms (doall (repeatedly 5 operating-room))]
    (dorun (map deref rooms))
    (totals)))

(defn auditor []
  (future
    (dotimes [_ 10]
      (println (totals)))))

(defn run-with-audit! []
  (init!)
  (println "Finding the variant:")
  (let [audit (auditor)
        rooms (doall (repeatedly 5 operating-room))]
    (dorun (map deref rooms))
    (deref audit)))

;; TODO rest of operation exercises

;; MACRO LAB

;; What I want to type: some expressions
;; What I want it to become: those expressions in sequence

(defmacro just-do-it
  "macro that just does what it takes in"
  [& body]
  `(do ~@body))

(defmacro execute-x [expr]
  `(do (clojure.pprint/pprint '~expr)
  ~expr
  (clojure.pprint/pprint ~expr)))












