import React from 'react';
import PropTypes from 'prop-types';
import cx from 'classnames';

import s from './Footer.module.scss';

class Footer extends React.Component {
    static propTypes = {
        className: PropTypes.string,
    };

    static defaultProps = {
        className: '',
    };

    render() {
        return (
            <footer className={cx(s.root, this.props.className)}>
                <div className={s.container}>

                </div>
            </footer>
        );
    }
}

export default Footer;
