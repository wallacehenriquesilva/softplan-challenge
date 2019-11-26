

import {connect} from 'react-redux';
import cx from 'classnames';
import React from 'react';
import PropTypes from 'prop-types';
import {Dropdown, DropdownItem, DropdownMenu, DropdownToggle, Nav, Navbar, NavItem,} from 'reactstrap';
import {NavLink} from 'react-router-dom';
import {logoutUser} from '../../actions/user';
import s from './Header.module.scss';
import {getCurrentUser} from "../../util/APIUtils";

class Header extends React.Component {
    constructor(props) {
        super(props);

    }

    componentDidMount() {
        getCurrentUser()
            .then(response => {
                this.setState({
                    currentUser: response,
                    authenticated: true,
                    loading: false
                });
                localStorage.setItem("currentUser", JSON.stringify(response));
            }).catch(error => {
            this.setState({
                loading: false
            });
        });
    }

    static propTypes = {
        sidebarToggle: PropTypes.func,
        dispatch: PropTypes.func.isRequired,
    };

    static defaultProps = {
        sidebarToggle: () => {
        },
    };

    state = {isOpen: false};

    toggleDropdown = () => {
        this.setState(prevState => ({
            isOpen: !prevState.isOpen,
        }));
    }

    doLogout = () => {
        this.props.dispatch(logoutUser());
    }

    render() {
        const {isOpen} = this.state;
        return (
            <Navbar className={s.root}>
                <Nav>
                    <NavItem
                        className={cx('visible-xs mr-4 d-sm-up-none', s.headerIcon, s.sidebarToggler)}
                        href="#"
                        onClick={this.props.sidebarToggle}
                    >
                        <i className="fa fa-bars fa-2x text-muted"/>
                    </NavItem>
                </Nav>
                <Nav className="ml-auto">

                    <Dropdown isOpen={isOpen} toggle={this.toggleDropdown}>
                        <DropdownToggle nav>
                            <img className={cx('rounded-circle mr-sm', s.adminPhoto)}
                                 src={JSON.parse(localStorage.getItem("currentUser")).picturePath}
                                 alt={JSON.parse(localStorage.getItem("currentUser")).name}/>
                            <span className="text-body">{JSON.parse(localStorage.getItem("currentUser")).name}</span>
                            <i className={cx('fa fa-angle-down ml-sm', s.arrow, {[s.arrowActive]: isOpen})}/>
                        </DropdownToggle>
                        <DropdownMenu style={{width: '100%'}}>
                            <DropdownItem>
                                <NavLink to="/app">Dashboard</NavLink>
                            </DropdownItem>
                            <DropdownItem>
                                <NavLink to="/app/persons">Pessoas</NavLink>
                            </DropdownItem>
                            <DropdownItem>
                                <NavLink to="/app/persons/new">Adicionar</NavLink>
                            </DropdownItem>
                            <DropdownItem>
                                <NavLink to="/" onClick={this.doLogout}>Sair</NavLink>
                            </DropdownItem>
                        </DropdownMenu>
                    </Dropdown>
                </Nav>
            </Navbar>
        );
    }
}

function mapStateToProps(state) {
    return {
        init: state.runtime.initialNow,
    };
}

export default connect(mapStateToProps)(Header);
