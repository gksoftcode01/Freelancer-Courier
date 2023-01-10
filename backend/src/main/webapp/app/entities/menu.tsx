import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/state-province">
        <Translate contentKey="global.menu.entities.stateProvince" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/country">
        <Translate contentKey="global.menu.entities.country" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/city">
        <Translate contentKey="global.menu.entities.city" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/user-rate">
        <Translate contentKey="global.menu.entities.userRate" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/item-types">
        <Translate contentKey="global.menu.entities.itemTypes" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/app-user">
        <Translate contentKey="global.menu.entities.appUser" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/flight">
        <Translate contentKey="global.menu.entities.flight" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/cargo-request-status">
        <Translate contentKey="global.menu.entities.cargoRequestStatus" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/cargo-request">
        <Translate contentKey="global.menu.entities.cargoRequest" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/cargo-request-details">
        <Translate contentKey="global.menu.entities.cargoRequestDetails" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/bid">
        <Translate contentKey="global.menu.entities.bid" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/ask">
        <Translate contentKey="global.menu.entities.ask" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
