declare module 'tough-cookie' {
  export interface Cookie {
    key: string;
    value: string;
    domain?: string;
    path?: string;
    expires?: Date;
    httpOnly?: boolean;
    secure?: boolean;
    sameSite?: string;
  }
  
  export class CookieJar {
    setCookie(cookie: string | Cookie, url: string, callback?: (err: Error | null) => void): void;
    getCookies(url: string, callback?: (err: Error | null, cookies: Cookie[]) => void): void;
  }
  
  export function parse(cookieString: string): Cookie;
  export function serialize(cookie: Cookie): string;
}
