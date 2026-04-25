// This DTO can be used for pages of products, of users... thats the reason because it´s generic
export interface PageDTO<T> {
    content: T[];
    page: {
        size: number;
        number: number;
        totalElements: number;
        totalPages: number;
    };
}