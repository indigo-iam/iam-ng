import { Component, OnInit } from '@angular/core';
import { RealmService } from 'src/app/services/realm.service';
import { UserService } from 'src/app/services/user.service';
import { ActivatedRoute, Router } from '@angular/router';
import { NgBlockUI, BlockUI } from 'ng-block-ui';
import { UserDTO } from 'src/app/models/user-dto';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.scss']
})
export class UserProfileComponent implements OnInit {

  realmName: string;
  userId: string;
  user = {
    metadata: {
      creationTime: '',
      lastUpdateTime: ''
    },
    id: -1,
    uuid: '',
    username: '',
    givenName: '',
    familyName: '',
    active: true,
    provisioned: true,
    emails: [],
    realm: {
      name: ''
    }
  };
  @BlockUI('userProfile') blockUIuserProfile: NgBlockUI;

  constructor(private realmService: RealmService, private userService: UserService, private route: ActivatedRoute, private router: Router) { }

  ngOnInit(): void {
    this.blockUIuserProfile.start();
    this.realmService.getCurrentRealm().subscribe(r => {
      this.realmName = r;
      console.log(r);
      this.route.paramMap.subscribe((paramMap) => {
        this.userId = paramMap.get('userid');
        console.log(this.userId);
        this.userService.getUser(this.realmName, this.userId).subscribe(
          (response: UserDTO) => {
            this.user = response;
            this.blockUIuserProfile.stop();
          },
          (error) => {
            this.blockUIuserProfile.stop();
            this.router.navigateByUrl('/404', {skipLocationChange: true});
          }
        );
      });
    });

  }

}
