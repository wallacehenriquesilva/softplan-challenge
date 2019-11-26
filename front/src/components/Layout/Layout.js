import React from 'react';
import cx from 'classnames';
import {Route, Switch, withRouter} from 'react-router';

import s from './Layout.module.scss';
import Header from '../Header';
import Footer from '../Footer';
import Sidebar from '../Sidebar';

import Dashboard from '../../pages/dashboard'
import Persons from '../../pages/persons/Persons'

class Layout extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            sidebarOpen: false,
        };
    }

    render() {
        return (
            <div className={s.root}>
                <Sidebar/>
                <div
                    className={cx(s.wrap, {[s.sidebarOpen]: this.state.sidebarOpen})}
                >
                    <Header
                        sidebarToggle={() =>
                            this.setState({
                                sidebarOpen: !this.state.sidebarOpen,
                            })
                        }
                    />
                    <main className={s.content}>
                        <Switch>
                            <Route path="/app/main" exact component={Dashboard}/>
                            <Route path="/app/persons" component={Persons}/>
                        </Switch>
                    </main>
                    <Footer/>
                </div>
            </div>
        );
    }
}

export default withRouter(Layout);
