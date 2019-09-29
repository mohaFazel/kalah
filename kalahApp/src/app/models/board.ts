export class Board {
  id: string;
  url: string;
  status: [{ [key: string]: number }];
  state: string;
  winner: string;
}
