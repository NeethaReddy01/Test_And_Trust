import {applyMiddleware, combineReducers, legacy_createStore} from "redux"
import thunk from "redux-thunk";
import authReducer from "./Auth/Reducer";
import customerProductReducer from "./Customers/Product/Reducer";
import cartReducer from "./Customers/Cart/Reducer";
import { orderReducer } from "./Customers/Order/Reducer";
import ReviewReducer from "./Customers/Review/Reducer";





const rootReducers=combineReducers({

    auth:authReducer,
    customersProduct:customerProductReducer,
    cart:cartReducer,
    order:orderReducer,
    review:ReviewReducer,

    // admin
    


});

export const store = legacy_createStore(rootReducers,applyMiddleware(thunk))