import { RealmDTO } from './realm-dto';
import { MetadataDTO } from './metadata-dto';

export interface UserDTO {
        metadata: MetadataDTO
        id: number,
        uuid: string,
        username: string,
        givenName: string,
        familyName: string,
        active: boolean,
        provisioned: boolean
        emails: [],
        realm: RealmDTO
}