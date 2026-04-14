export function Head() {
    const baseUrl = import.meta.env.BASE_URL;

    return (
        <>
            <title>ReMarket+ | Compra y Vende</title>
            <meta name="description" content="Prototipo del frontend de ReMarket+" />
            <link
                href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
                rel="stylesheet"
            />
            <link
                href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css"
                rel="stylesheet"
            />
            <link href={`${baseUrl}css/styles.css`} rel="stylesheet" />
            <script
                src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"
                defer
            />
            <link rel="icon" href={`${baseUrl}assets/Logo_Remarket.png`} type="image/png" />
        </>
    );
}