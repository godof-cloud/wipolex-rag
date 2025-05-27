
export enum PartyType {
        complainant = 'reqParty' ,
        respondent = 'respParty'
    }

    export enum PrivateType {
        INDAUTOR = 'indautor' ,
        DNDA = 'dnda'
    }

    
export class Party {
    id: number;
    name: string;
    address: string;
    telephone: string;
    email: string;
    type: PartyType;
    representedBy: string;
    addressRepresentative: string;
    telephoneRepresentative: string;
    emailRepresentative: string;
}