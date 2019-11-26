export const SET_RUNTIME_VARIABLE = 'SET_RUNTIME_VARIABLE';

export const TOGGLE_SIDEBAR = 'TOGGLE_SIDEBAR';
export const OPEN_SIDEBAR = 'OPEN_SIDEBAR';
export const CLOSE_SIDEBAR = 'CLOSE_SIDEBAR';


export const API_BASE_URL = 'http://165.227.3.54:2207';
export const ACCESS_TOKEN = 'id_token';
export const API_BASE_URL_PERSON = API_BASE_URL + '/api/v1/service/data/persons/';

export const OAUTH2_REDIRECT_URI = 'http://localhost:3000/oauth2/redirect'

export const GOOGLE_AUTH_URL = API_BASE_URL + '/oauth2/authorize/google?redirect_uri=' + OAUTH2_REDIRECT_URI;
export const FACEBOOK_AUTH_URL = API_BASE_URL + '/oauth2/authorize/facebook?redirect_uri=' + OAUTH2_REDIRECT_URI;
export const GITHUB_AUTH_URL = API_BASE_URL + '/oauth2/authorize/github?redirect_uri=' + OAUTH2_REDIRECT_URI;

