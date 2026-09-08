import { SeatDTO } from "./seat-dto.model";
import { Train } from "./train.model";

export class Coaches{
    public coachId : number = 0;
    public coachNumber : string = '';
    public classType : string = '';
    public totalSeats : number = 0;
    // public racCapacity : number = 0;
    // public waitingCapacity : number = 0;
    // public currentRacCount : number = 0;
    // public currentWaitingCount : number = 0;
    // public train : Train = new Train();
    public seats: SeatDTO[] = [];
}