import React from 'react';
import "./Advertisment.css"

const Advertisment = () => {
  return (
    <section>
      <a className="advertisment-root" href="https://playstorereply.com" target="_blank" rel="noopener">
        <svg style={{ width: "24px", height: "24px", marginRight: "16px" }} viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-megaphone"><path d="m3 11 18-5v12L3 14v-3z" /><path d="M11.6 16.8a3 3 0 1 1-5.8-1.6" /></svg>
        Manage your app's reviews on Google Play using AI
        <svg style={{ width: "24px", height: "24px", marginLeft: "16px" }} viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-move-right"><path d="M18 8L22 12L18 16"/><path d="M2 12H22"/></svg>
      </a>
    </section>
  );
}

export default Advertisment