export enum SortingProperty {
  MEETING_START_DATE = 'meetingDate',
  CLAIMANT = 'claimant',
  RESPONDENT = 'respondent',
  CASE_NO = 'caseNo',
  MEETING_NO = 'meetingNumber',
  SESSION_NO = 'sessionNumber',
  STATUS = 'status'
}

export enum SortDirection {
  ASC = 'ASC',
  DESC = 'DESC'
}

export interface SessionFilterDto {
  sortingDto: SortingDto;
}

export interface SortingDto {
  sortingProperty: SortingProperty;
  direction: SortDirection;
}