import React from 'react';
import ReactDOM from 'react-dom';
import {applyMiddleware, createStore} from 'redux';
import {Provider} from 'react-redux'
import ReduxThunk from 'redux-thunk'
import * as serviceWorker from './serviceWorker';
import axios from 'axios';

import App from './components/App';
import config from './config';
import reducers from './reducers';

axios.defaults.baseURL = config.baseURLApi;
axios.defaults.headers.common['Content-Type'] = "application/json";
const token = localStorage.getItem('token');
if (token) {
    axios.defaults.headers.common['Authorization'] = "Bearer " + token;
}

const store = createStore(
    reducers,
    applyMiddleware(ReduxThunk)
);

ReactDOM.render(
    <Provider store={store}>
        <App/>
    </Provider>,
    document.getElementById('root')
);

serviceWorker.unregister();
