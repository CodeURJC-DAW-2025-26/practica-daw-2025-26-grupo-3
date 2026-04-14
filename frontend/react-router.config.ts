import type { Config } from "@react-router/dev/config";

const isProd = process.env.NODE_ENV === "production";

export default {
  // Config options...
  // Server-side render by default, to enable SPA mode set this to `false`
  basename: isProd ? "/new/" : "/",
  ssr: false,
} satisfies Config;
