import React from 'react';
import {Route, Switch, withRouter} from 'react-router';

import PersonsList from './list/PersonsList';
import PersonNew from './new/PersonNew';

class Persons extends React.Component {
    render() {
        return (
            <Switch>
                <Route path="/app/persons" exact component={PersonsList}/>
                <Route path="/app/persons/new" exact component={PersonNew}/>
            </Switch>
        );
    }
}

export default withRouter(Persons);
