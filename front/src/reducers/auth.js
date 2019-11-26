import {LOGIN_FAILURE, LOGIN_REQUEST, LOGIN_SUCCESS, LOGOUT_SUCCESS, SIGNUP_REQUEST,} from '../actions/user';

const token = localStorage.getItem('token');
export default function auth(state = {
    isFetching: false,
    isAuthenticated: !!token,
}, action) {
    switch (action.type) {
        case LOGIN_REQUEST:
            return Object.assign({}, state, {
                isFetching: true,
                isAuthenticated: false,
            });
        case SIGNUP_REQUEST:
            return Object.assign({}, state, {
                isFetching: true,
                isAuthenticated: false,
            });
        case LOGIN_SUCCESS:
            return Object.assign({}, state, {
                isFetching: false,
                isAuthenticated: true,
                errorMessage: '',
            });
        case LOGIN_FAILURE:
            return Object.assign({}, state, {
                isFetching: false,
                isAuthenticated: false,
                errorMessage: action.payload,
            });
        case LOGOUT_SUCCESS:
            return Object.assign({}, state, {
                isAuthenticated: false,
            });
        default:
            return state;
    }
}
