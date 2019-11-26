import React from 'react';
import {connect} from 'react-redux';
import {Redirect, Route, Switch} from 'react-router';
import {HashRouter} from 'react-router-dom';
import {ToastContainer} from 'react-toastify';


import '../styles/theme.scss';
import LayoutComponent from '../components/Layout';
import Login from '../pages/login';
import {logoutUser} from '../actions/user';
import OAuth2RedirectHandler from "./oauth2/OAuth2RedirectHandler";


const PrivateRoute = ({dispatch, component, ...rest}) => {
    if (!Login.isAuthenticated(localStorage.getItem('id_token'))) {
        dispatch(logoutUser());
        return (<Redirect to="/login"/>)
    } else {
        return ( // eslint-disable-line
            <Route {...rest} render={props => (React.createElement(component, props))}/>
        );
    }
};

const CloseButton = ({closeToast}) => <i onClick={closeToast} className="la la-close notifications-close"/>

class App extends React.PureComponent {
    render() {
        return (
            <div>
                <ToastContainer
                    autoClose={5000}
                    hideProgressBar
                    closeButton={<CloseButton/>}
                />
                <HashRouter>
                    <Switch>
                        <Route path="/source" component={() => {
                            window.location.href = 'https://github.com/wallacehenriquesilva/softplan-challenge';
                        }}/>
                        <Route path="/" exact component={OAuth2RedirectHandler}/>
                        <Route path="/app" exact render={() => <Redirect to="/app/main"/>}/>
                        <PrivateRoute path="/app" dispatch={this.props.dispatch} component={LayoutComponent}/>
                        <Route path="/documentation" exact
                               render={() => <Redirect to="/documentation/getting-started/overview"/>}/>
                        <Route path="/login" exact component={Login}/>
                        <Route path="/oauth2/redirect" component={OAuth2RedirectHandler}/>

                    </Switch>
                </HashRouter>
            </div>

        );
    }
}

const mapStateToProps = state => ({
    isAuthenticated: state.auth.isAuthenticated,
});

export default connect(mapStateToProps)(App);
