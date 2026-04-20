export class HttpError extends Error {
    status: number;

    constructor(status: number, message?: string) {
        super(message ?? `HTTP ${status}`)
        this.status = status;
    }
}