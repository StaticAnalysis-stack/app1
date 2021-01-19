import { Pipe, PipeTransform } from '@angular/core';
import{UserList} from './user-list'

@Pipe({
  name:'userList'
})
export class UserListPipe implements PipeTransform {

  transform(user:UserList[],searchTerm:string):UserList[]
   {
    if(!user||!searchTerm)
    {
      return user;
    }

    return user.filter(user=> 
      (user.firstName.toLowerCase().indexOf(searchTerm.toLowerCase())!==-1)|| (user.middleName.toLowerCase().indexOf(searchTerm.toLowerCase())!==-1)
      ||(user.lastName.toLowerCase().indexOf(searchTerm.toLowerCase())!==-1)||(user.emailid.toLowerCase().indexOf(searchTerm.toLowerCase())!==-1));
  }

}
