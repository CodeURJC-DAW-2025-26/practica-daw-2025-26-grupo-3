export function Spinner() {
    return (
        <div className="index-loading-screen" aria-live="polite" aria-busy="true">
            <span className="index-loading-spinner" role="status" aria-label="Cargando" />
        </div>
    );
}