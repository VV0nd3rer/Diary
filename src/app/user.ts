export class User {
    constructor(
        public username: string,
        public password: string
    ) {  }
}
export class LoginStatus {
    constructor(
        public code: string,
        public message: string
    ) { }
}