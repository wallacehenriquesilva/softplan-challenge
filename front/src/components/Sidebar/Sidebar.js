import React from 'react';
import {connect} from 'react-redux';
import {Link, withRouter} from 'react-router-dom';

import Icon from '../Icon';
import LinksGroup from './LinksGroup/LinksGroup';

import s from './Sidebar.module.scss';

const Sidebar = () => (
    <nav className={s.root}>
        <header className={s.logo}>
            <Link to="/app/main">
                <Icon glyph="logo"/>
            </Link>
        </header>
        <ul className={s.nav}>
            <LinksGroup
                header="Dashboard"
                headerLink="/app/main"
                glyph="dashboard"
            />
            <LinksGroup
                header="Pessoas"
                headerLink="/app/persons"
                glyph="tables"
            />
            <LinksGroup
                header="Adicionar"
                headerLink="/app/persons/new"
                glyph="components"
            />
        </ul>
    </nav>
);

function mapStateToProps(store) {
    return {
        sidebarOpened: store.navigation.sidebarOpened,
        sidebarStatic: store.navigation.sidebarStatic,
    };
}

export default withRouter(connect(mapStateToProps)(Sidebar));
