import { SeatStatus } from "./seat-status.enum";

export class Seat{
    public id : number = 0;
    public seatNumber : string = '';
    public status : SeatStatus = SeatStatus.AVAILABLE;
}