interface ErrorCardProps {
    message: string;
    className?: string;
}

export function ErrorCard({ message, className }: ErrorCardProps) {
    return (
        <div
            className={className}
            style={{
                backgroundColor: "#fee2e2",
                color: "#b91c1c",
                padding: "12px 16px",
                borderRadius: "8px",
                border: "1px solid #fca5a5",
                fontSize: "0.95rem",
            }}
        >
            <strong>{message}</strong>
        </div>
    );
}