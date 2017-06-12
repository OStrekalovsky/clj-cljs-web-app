(ns demo.ui
  (:require
    [ajax.core :refer [GET POST]]
    [reagent.core :as reagent :refer [atom]]
    [re-frame.core :as rf :refer [reg-event-db
                                  reg-sub
                                  dispatch
                                  dispatch-sync
                                  subscribe]]))

(rf/reg-event-db
  :initialize
  (fn [_ _]
    {::user-text       "Oleg"
     ::server-greeting "No greeting"
     }
    ))

(rf/reg-event-db ::response-received
                 (fn [db [_ new-value]]
                   (assoc db ::server-greeting new-value)))

(rf/reg-event-db ::user-change-text
                 (fn [db [_ new-value]]
                   (assoc db ::user-text new-value)))

(rf/reg-sub ::user-text
            (fn [db _]
              (db ::user-text)))

(rf/reg-sub ::server-greeting
            (fn [db _]
              (db ::server-greeting)
              ))

(defn error-handler [{:keys [status status-text]}]
  (.log js/console (str "something bad happened: " status " " status-text))
  (rf/dispatch [::response-received "Shit happends :(. Try again :)"])
  )


(defn handler [response]
  (.log js/console (str "response received=" response))
  (rf/dispatch [::response-received response])
  )

(defn send-command [text]
  (let [address (str "http://localhost:3000/" text)]
    (.log js/console (str "addres=" address))
    (GET address {:handler       handler
                  :error-handler error-handler
                  })
    )
  (rf/dispatch [::response-received "Waiting for a second ..."])
  )

(defn- application []
  [:div "Enter command:"
   [:input {
            :type      "text"
            :on-change #(rf/dispatch [::user-change-text (-> % .-target .-value)])
            :value     @(rf/subscribe [::user-text])}
    ]
   [:div
    {:style {:color "red"}}
    (str "Your command:" @(rf/subscribe [::user-text]))]
   [:input {:type     "button"
            :value    "Send command to the server"
            :on-click #(send-command @(rf/subscribe [::user-text]))
            }]
   [:div @(rf/subscribe [::server-greeting])]
   ]
  )


(defn ^:export main [target]
  (def render
    (fn []
      (reagent/render [application]
                      (.getElementById js/document target))))
  (rf/dispatch-sync [:initialize])
  (render))
