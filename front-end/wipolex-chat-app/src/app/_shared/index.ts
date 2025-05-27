function capitaliseFirst (first: string, text: string): string {
  text = text[0].charAt(0).toLowerCase() + text.slice(1);
  return `${first.charAt(0).toUpperCase() + first.slice(1)} ${text}`;  
}
export enum ROUTE {
    IDEM = '#',
    P_FONTS = 'fonts', 
    
    P_HOME = 'home-page',

    P_INPUT = 'input-page', 
    INPUT_TEXT = 'input-text',
    INPUT_TEXT_AREA = 'input-text-area',
    INPUT_FILE = 'input-file',
    INPUT_DATE = 'input-date',
    SELECT_BOOLEAN_CHECKBOX = 'select-boolean-checkbox',
    SELECT_MANY_CHECKBOX = 'select-many-checkbox',
    SELECT_ONE_MENU = 'select-one-menu',
    SELECT_MANY_MENU = 'select-many-menu',
 
    P_ACTIONS = 'actions-page', 
    BUTTONS = 'buttons',
    ACTION_BAR = 'action-bar',
    NAVIGATION_FLOATER = 'navigation-floater',
    
    P_CONTAINERS = 'containers-page', 
    EDIT_PANEL = 'edit-panel',
    VIEW_PANEL = 'view-panel',
    SECTION = 'section',
    SLOTS = 'slot',
    STEP = 'step',
    
    P_INFO = 'info-page',
    OUTPUT_FIELD = 'output-field',
    INFOBOX = 'infobox',
    NOTIFICATION_BAR = 'notification-bar',
    VALIDATION = 'validation',
    ISSUE_BLOCK = 'issue-block',
    
    P_GLOBAL = 'global-page',
    PAGE_HEADER = 'page-header',
    PAGE_SECTION = 'page-section',

    P_SAMPLE = 'sample-page',
    FORMS_N_INPUTS = 'forms-and-inputs',

    P_CONTENT = 'content-page',
    ARTICLE = 'article',
    BOX = 'box',
    PAY_BOX = 'pay-box',
    LANDING = 'landing',
    VIDEO = 'video',
    VIEW = 'view',

  }
export interface SubLink {
    label: string; route: ROUTE;
  }
export const sublink = (label: string, route: ROUTE): SubLink => {
    return {label, route};
}

export const isROKMCST = function isROKMCST(collaborationData)  {
  if (collaborationData === '/rok/mcst') {
    return true;
  }
  return false;
}